public interface IExternalDataService
{
    /// <summary>
    /// Имитирует асинхронный запрос к API пользователя.
    /// Требуемая имитация задержки: 2000 мс.
    /// </summary>
    Task<string> GetUserDataAsync(int userId);

    /// <summary>
    /// Имитирует асинхронный запрос к API заказов.
    /// Требуемая имитация задержки: 3000 мс.
    /// </summary>
    Task<string> GetUserOrdersAsync(int userId);

    /// <summary>
    /// Имитирует асинхронный запрос к API рекламного контента.
    /// Требуемая имитация задержки: 1000 мс.
    /// </summary>
    Task<string> GetAdsAsync();
}

public class PagePayload
{
    public string UserData { get; set; }
    public string OrderData { get; set; }
    public string AdData { get; set; }

    public override string ToString()
    {
        return $"--- Агрегированный результат --- \nПользователь: {UserData}\nЗаказы: {OrderData}\nРеклама: {AdData}\n-----------------";
    }
}

public interface IPageAggregator
{
    /// <summary>
    /// Метод 1: Последовательная стратегия.
    /// Выполняет запросы к IExternalDataService строго последовательно,
    /// ожидая завершения каждого предыдущего запроса.
    /// </summary>
    Task<PagePayload> LoadPageDataSequentialAsync(int userId);

    /// <summary>
    /// Метод 2: Параллельная стратегия.
    /// Инициирует все запросы одновременно и асинхронно
    /// ожидает их общего завершения (c использованием Task.WhenAll).
    /// </summary>
    Task<PagePayload> LoadPageDataParallelAsync(int userId);
}