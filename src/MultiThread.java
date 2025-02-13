import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class MultiThread
{

    private static final String INPUT_FILE = "random_words.txt";
    private static final int CHUNK_SIZE = 1024 * 1024; // 1 MB chunk size
    private static final int BATCH_SIZE = 1000; // Process 1000 words per task
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static void processBatch(String[] words, Map<Integer, LongAdder> lengthMap, LongAdder lenSum,
                                     LongAdder count)
    {
        for (String word : words)
        {
            if (word != null)
            {
                int len = word.length();
                lengthMap.computeIfAbsent(len, k -> new LongAdder()).increment();
                lenSum.add(len);
                count.add(1);
            }
        }
    }

    private static void processChunk(ByteBuffer buffer, Map<Integer, LongAdder> lengthMap, LongAdder lenSum,
                                     LongAdder count)
    {
        StringBuilder wordBuilder = new StringBuilder();
        String[] batch = new String[BATCH_SIZE];
        int batchIndex = 0;

        while (buffer.hasRemaining())
        {
            char ch = (char) buffer.get();
            if (ch == '\n' || ch == '\r')
            {
                if (wordBuilder.length() > 0)
                {
                    batch[batchIndex++] = wordBuilder.toString();
                    wordBuilder.setLength(0);

                    if (batchIndex == BATCH_SIZE)
                    {
                        processBatch(batch, lengthMap, lenSum, count);
                        batchIndex = 0; // Reset batch index
                    }
                }
            }
            else
            {
                wordBuilder.append(ch); // Append character to the current word
            }
        }
        // Process the last batch if it's not empty
        if (batchIndex > 0)
        {
            processBatch(batch, lengthMap, lenSum, count);
        }
    }

    public static void main(String[] args)
    {
        Map<Integer, LongAdder> lengthMap = new ConcurrentHashMap<>();
        LongAdder lenSum = new LongAdder();
        LongAdder count = new LongAdder();

        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        long start = System.currentTimeMillis();

        try (RandomAccessFile file = new RandomAccessFile(INPUT_FILE, "r");
             FileChannel channel = file.getChannel())
        {
            long fileSize = channel.size();
            long position = 0;

            while (position < fileSize)
            {
                long chunkSize = Math.min(CHUNK_SIZE, fileSize - position);
                ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, position, chunkSize);
                position += chunkSize;
                // Submit the chunk for processing
                service.submit(() -> processChunk(buffer, lengthMap, lenSum, count));
            }
        }
        catch (IOException e)
        {
            System.err.println("Error reading file: " + e.getMessage());
        }
        finally
        {
            service.shutdown(); // Shutdown the executor service
            try
            {
                // Wait for all tasks to complete or timeout after 1 minute
                if (!service.awaitTermination(1, TimeUnit.MINUTES))
                {
                    service.shutdownNow(); // Force shutdown if tasks don't complete in time
                }
            }
            catch (InterruptedException e)
            {
                service.shutdownNow(); // Force shutdown if interrupted
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Total Time Taken = " + (end - start) + "ms");
        System.out.println("Length to Count Map: " + lengthMap);
        System.out.println("Average Length: " + (1.0 * lenSum.sum() / count.sum()));
    }
}