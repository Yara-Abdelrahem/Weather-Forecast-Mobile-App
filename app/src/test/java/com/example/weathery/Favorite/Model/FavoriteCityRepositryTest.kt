import com.example.weathery.Favorite.Model.FakeFavoriteCityDataSource
import com.example.weathery.Favorite.Model.FavoriteCity
import com.example.weathery.Favorite.Model.FavoriteCityRepositry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class FavoriteCityRepositryTest {
    private lateinit var localDataSource: FakeFavoriteCityDataSource
    private lateinit var repository: FavoriteCityRepositry

    @Before
    fun setup() {
        localDataSource = FakeFavoriteCityDataSource()
//        localDataSource = FakeFavoriteCityDataSource()
//        localDataSource = mock()
            repository = FavoriteCityRepositry(localDataSource)
    }

    @Test
    fun `getFavCities returns expected list`() = runBlocking {
        val dummyList = listOf(
            FavoriteCity(id = 1, city_name = "Cairo", city_lat = 31.0, city_lon = 30.0),
            FavoriteCity(id = 2, city_name = "Riyadh", city_lat = 29.0, city_lon = 29.0)
        )
        dummyList.forEach { localDataSource.insertFavCity(it) }

        val result = repository.getFavCities()

        assertEquals(dummyList, result)
    }

    @Test
    fun `insertFavCity adds city to local data source`() = runBlocking {
        val city = FavoriteCity(id = 3, city_name = "Dubai", city_lat = 10.0, city_lon = 10.0)

        repository.insertFavCity(city)

        val result = repository.getFavCities()
        assertEquals(listOf(city), result)
    }

    @Test
    fun `deleteFavCity removes city from local data source`() = runBlocking {
        val city = FavoriteCity(id = 4, city_name = "Doha", city_lat = 25.0, city_lon = 51.0)

        localDataSource.insertFavCity(city) // insert manually
        repository.deleteFavCity(city)

        val result = repository.getFavCities()
        assertEquals(emptyList<FavoriteCity>(), result)
    }
}
