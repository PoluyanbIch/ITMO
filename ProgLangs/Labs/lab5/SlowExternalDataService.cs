public class SlowExternalDataService : IExternalDataService
{
    public async Task<string> GetUserDataAsync(int userId)
    {
        Console.WriteLine($"[{DateTime.Now:HH:mm:ss.fff}] Начало выполнения GetUserDataAsync для пользователя {userId}");
        
        await Task.Delay(2000);
        
        Console.WriteLine($"[{DateTime.Now:HH:mm:ss.fff}] Завершение выполнения GetUserDataAsync для пользователя {userId}");
        return $"Данные пользователя {userId}";
    }

    public async Task<string> GetUserOrdersAsync(int userId)
    {
        Console.WriteLine($"[{DateTime.Now:HH:mm:ss.fff}] Начало выполнения GetUserOrdersAsync для пользователя {userId}");
        
        await Task.Delay(3000);
        
        Console.WriteLine($"[{DateTime.Now:HH:mm:ss.fff}] Завершение выполнения GetUserOrdersAsync для пользователя {userId}");
        return $"Заказы пользователя {userId}";
    }

    public async Task<string> GetAdsAsync()
    {
        Console.WriteLine($"[{DateTime.Now:HH:mm:ss.fff}] Начало выполнения GetAdsAsync");
        
        await Task.Delay(1000);
        
        Console.WriteLine($"[{DateTime.Now:HH:mm:ss.fff}] Завершение выполнения GetAdsAsync");
        return "Рекламный контент";
    }
}

public class PageAggregatorService : IPageAggregator
{
    private readonly IExternalDataService _externalDataService;

    public PageAggregatorService(IExternalDataService externalDataService)
    {
        _externalDataService = externalDataService;
    }

    public async Task<PagePayload> LoadPageDataSequentialAsync(int userId)
    {
        Console.WriteLine($"\n=== Начало последовательной загрузки данных для пользователя {userId} ===");
        
        var userData = await _externalDataService.GetUserDataAsync(userId);
        var orderData = await _externalDataService.GetUserOrdersAsync(userId);
        var adData = await _externalDataService.GetAdsAsync();

        Console.WriteLine($"=== Завершение последовательной загрузки данных ===\n");
        
        return new PagePayload
        {
            UserData = userData,
            OrderData = orderData,
            AdData = adData
        };
    }

    public async Task<PagePayload> LoadPageDataParallelAsync(int userId)
    {
        Console.WriteLine($"\n=== Начало параллельной загрузки данных для пользователя {userId} ===");
        
        var userDataTask = _externalDataService.GetUserDataAsync(userId);
        var orderDataTask = _externalDataService.GetUserOrdersAsync(userId);
        var adDataTask = _externalDataService.GetAdsAsync();

        await Task.WhenAll(userDataTask, orderDataTask, adDataTask);

        Console.WriteLine($"=== Завершение параллельной загрузки данных ===\n");
        
        return new PagePayload
        {
            UserData = userDataTask.Result,
            OrderData = orderDataTask.Result,
            AdData = adDataTask.Result
        };
    }
}