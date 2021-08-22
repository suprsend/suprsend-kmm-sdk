package app.suprsend.android.android

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductVo(
    val id: String,
    val title: String,
    val amount: Double,
    val url: String
) : BaseItem(), Parcelable {
    fun getPrice(): String {
        return "$amount/- RS"
    }

    override fun getItemId(): String {
        return id
    }
}
