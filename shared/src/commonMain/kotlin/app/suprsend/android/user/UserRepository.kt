package app.suprsend.android.user

import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import app.suprsend.android.UserModel
import kotlinx.coroutines.flow.Flow

class UserRepository(
    val userDao: UserDao,
    val userNetwork: UserNetwork
) {

    suspend fun insertUser(userModel: UserModel) {
        userDao.insert(userModel.id, userModel.name, userModel.company!!)
    }

    suspend fun getUsers(): Flow<List<UserModel>> {
        return userDao.select()
    }

    suspend fun makeRemoteCall(): String? {
        return userNetwork.getUsers()
    }

    companion object {
        fun getInstance(): UserRepository {
            val userDao = UserDao(GLOBAL_SUPR_SEND_DATABASE_WRAPPER.get()!!.suprSendDatabase)
            val userNetwork = UserNetwork()
            return UserRepository(userDao,userNetwork)
        }
    }
}