package app.suprsend.android.user

import app.suprsend.android.SuprSendDatabase
import app.suprsend.android.UserModel
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow


class UserDao(suprSendDatabase: SuprSendDatabase) {

    private val db = suprSendDatabase.userQueries

    internal fun insert(id: String, name: String, company: Company) {
        db.insertItem(
            id = id,
            name = name,
            company = company
        )
    }

    internal fun select(): Flow<List<UserModel>> = db.selectAll().asFlow().mapToList()
}