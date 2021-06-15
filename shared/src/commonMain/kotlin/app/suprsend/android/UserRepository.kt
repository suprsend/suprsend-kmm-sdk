package app.suprsend.android

class UserRepository(
    val userDao: UserDao,
    val userNetwork: UserNetwork
) {

    suspend fun insertUser(userModel: UserModel) {
        userDao.insert(userModel.id, userModel.name, userModel.company!!)
    }

    suspend fun getUsers(): List<UserModel> {
        return userDao.select()
    }

    suspend fun makeRemoteCall(): String? {
        return userNetwork.getUsers()
    }

    companion object {
        fun getInstance(): UserRepository {
            val userDao = UserDao(globalDatabase.get()!!.database)
            val userNetwork = UserNetwork()
            return UserRepository(userDao,userNetwork)
        }
    }
}