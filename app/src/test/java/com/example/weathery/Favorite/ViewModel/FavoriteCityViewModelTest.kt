import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weathery.Favorite.Model.FakeFavoriteCityDataSource
import com.example.weathery.Favorite.Model.FavoriteCity
import com.example.weathery.Favorite.Model.FavoriteCityRepositry
import com.example.weathery.Favorite.ViewModel.FavoriteCityViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteCityViewModelTest {

    // Executes LiveData immediately
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeDataSource: FakeFavoriteCityDataSource
    private lateinit var repository: FavoriteCityRepositry
    private lateinit var viewModel: FavoriteCityViewModel

    @Before
    fun setup() {
        fakeDataSource = FakeFavoriteCityDataSource()
        repository = FavoriteCityRepositry(fakeDataSource)
        viewModel = FavoriteCityViewModel(repository)
    }

    @Test
    fun `getAllFavCity updates live data and returns list`() = runBlocking {
        val city1 = FavoriteCity(id = 1, city_name = "Cairo", city_lat = 30.0, city_lon = 31.0)
        val city2 = FavoriteCity(id = 2, city_name = "Riyadh", city_lat = 24.0, city_lon = 24.0)

        fakeDataSource.insertFavCity(city1)
        fakeDataSource.insertFavCity(city2)

        val result = viewModel.getAllFavCity()

        assertEquals(listOf(city1, city2), result)
        assertEquals(listOf(city1, city2), viewModel.Fav_city_ret.value)
    }

    @Test
    fun `insertFavCity adds a city`() = runBlocking {
        val city = FavoriteCity(id = 3, city_name = "Dubai", city_lat = 30.3, city_lon = 55.5)

        viewModel.insertFavCity(city)
        val result = viewModel.getAllFavCity()
        assertEquals(listOf(city), result)
    }

    @Test
    fun `deletFavCity removes a city`() = runBlocking {
        val city = FavoriteCity(id = 4, city_name = "Doha", city_lat = 25.0, city_lon = 25.0)

        fakeDataSource.insertFavCity(city)
        viewModel.deletFavCity(city)

        val result = viewModel.getAllFavCity()
        assertEquals(emptyList<FavoriteCity>(), result)
    }
}
