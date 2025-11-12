// Program.cs
using System.Diagnostics;

class Program
{
    static async Task Main(string[] args)
    {
        Console.WriteLine("=== Лабораторная работа: Сравнение последовательного и параллельного выполнения асинхронных операций ===\n");

        var externalService = new SlowExternalDataService();
        var aggregatorService = new PageAggregatorService(externalService);

        var userId = 123;
        var stopwatch = new Stopwatch();

        Console.WriteLine("1. ТЕСТИРОВАНИЕ ПОСЛЕДОВАТЕЛЬНОГО ПОДХОДА:");
        stopwatch.Start();
        var sequentialResult = await aggregatorService.LoadPageDataSequentialAsync(userId);
        stopwatch.Stop();
        
        Console.WriteLine(sequentialResult.ToString());
        Console.WriteLine($"Время выполнения последовательного подхода: {stopwatch.ElapsedMilliseconds} мс\n");

        await Task.Delay(1000);

        Console.WriteLine("2. ТЕСТИРОВАНИЕ ПАРАЛЛЕЛЬНОГО ПОДХОДА:");
        stopwatch.Restart();
        var parallelResult = await aggregatorService.LoadPageDataParallelAsync(userId);
        stopwatch.Stop();
        
        Console.WriteLine(parallelResult.ToString());
        Console.WriteLine($"Время выполнения параллельного подхода: {stopwatch.ElapsedMilliseconds} мс\n");

        Console.WriteLine("3. АНАЛИЗ РЕЗУЛЬТАТОВ:");
        Console.WriteLine($"Ожидаемое время последовательного выполнения: ~6000 мс (2000 + 3000 + 1000)");
        Console.WriteLine($"Ожидаемое время параллельного выполнения: ~3000 мс (максимальная задержка)");
        Console.WriteLine($"\nВывод: Параллельный подход эффективнее на {6000 - stopwatch.ElapsedMilliseconds} мс!");

        Console.WriteLine("\nНажмите любую клавишу для выхода...");
        Console.ReadKey();
    }
}