package fr.nodesigner.meaoo.mqtt.androidsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.androidsample.activity.format
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Option
import kotlinx.android.synthetic.main.path_view_item.view.ivTransport
import kotlinx.android.synthetic.main.path_view_item.view.tvTarget
import kotlinx.android.synthetic.main.path_view_item.view.tvTime
import kotlinx.android.synthetic.main.path_view_item.view.tvTitle
import kotlin.math.roundToInt

class PathAdapter(
    private val context: Context,
    private val itemClickListener: (Option) -> Unit
) : RecyclerView.Adapter<PathAdapter.ViewHolder>() {

    var options: List<Option> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.path_view_item, parent, false)
        return ViewHolder(layout, itemClickListener)
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]

        holder.apply {
            val iconRes = when (option) {
                is Option.WalkToTarget -> R.drawable.ic_walk
                is Option.WalkToSubway -> R.drawable.ic_walk
                is Option.BikeToTarget -> R.drawable.ic_bike
                is Option.CallTaxi -> R.drawable.ic_car
                is Option.UseTaxiToTarget -> R.drawable.ic_car
                is Option.UseSubwayToTarget -> R.drawable.ic_subway
            }

            val title = when (option) {
                is Option.WalkToTarget -> R.string.walk_to_target
                is Option.WalkToSubway -> R.string.walk_to_subway
                is Option.BikeToTarget -> R.string.bike_to_target
                is Option.CallTaxi -> R.string.call_taxi
                is Option.UseTaxiToTarget -> R.string.use_taxi_to_target
                is Option.UseSubwayToTarget -> R.string.use_subway_to_target
            }

            holder.ivTransport.setImageDrawable(context.getDrawable(iconRes))
            holder.tvTitle.text = context.getString(title)
            holder.tvTarget.text =
                "x: ${option.coordinate.x.format(2)}, y: ${option.coordinate.y.format(2)}"
            holder.tvTime.text = formatTime(option.cost)
        }
    }

    private fun formatTime(time: Double): String {
        var minutes = time.roundToInt()
        val hours = minutes / 60
        minutes %= 60

        return "${if (hours < 10) "0" else ""}$hours:${if (minutes < 10) "0" else ""}$minutes"
    }

    inner class ViewHolder(view: View, itemClickListener: (Option) -> Unit) :
        RecyclerView.ViewHolder(view) {

        val ivTransport: ImageView = view.ivTransport
        val tvTitle: TextView = view.tvTitle
        val tvTarget: TextView = view.tvTarget
        val tvTime: TextView = view.tvTime

        init {
            view.setOnClickListener {
                itemClickListener.invoke(options[adapterPosition])
            }
        }
    }
}
