import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.Favorite.Model.FavoriteCity
import com.example.weathery.Favorite.IFavClickListener
import com.example.weathery.databinding.FavCityItemBinding

class FavCityAdapter(
    private val items: MutableLiveData<List<FavoriteCity>>,
    private val listener: IFavClickListener
) : RecyclerView.Adapter<FavCityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = items.value?.get(position) ?: return

        holder.binding.cityName.text = city.city_name

        holder.binding.cityName.setOnClickListener {
            Log.i("Fav city click", "${city.city_name} clicked")
            listener.onNameCityClick(city)
        }

        holder.binding.btnRemoveCity.setOnClickListener {
            items.value?.let {
                if (position >= 0 && position < it.size) {
                    listener.onDeleteFavCityClick(city)
                    val mutableList = it.toMutableList()
                    mutableList.removeAt(position)
                    items.value = mutableList
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.value?.size ?: 0
    }

    class ViewHolder(val binding: FavCityItemBinding) : RecyclerView.ViewHolder(binding.root)
}
