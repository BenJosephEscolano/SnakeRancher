import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csit284.snakerancher.R
import com.csit284.snakerancher.util.User

class LeaderboardAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<LeaderboardAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.usernameText)
        val scoreText: TextView = itemView.findViewById(R.id.scoreText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.usernameText.text = user.username
        holder.scoreText.text = user.highScore.toString()
    }

    override fun getItemCount(): Int = userList.size
}


