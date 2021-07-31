package app.suprsend.android.user

internal class UserRepository
constructor(
    private val userLocalDatasource: UserLocalDatasource = UserLocalDatasource(),
    private val userRemoteDatasource: UserRemoteDatasource = UserRemoteDatasource()
) : UserRepositoryContract {

    override fun identify(uniqueId: String) {
        userLocalDatasource.identify(uniqueId)
    }

    override fun getIdentity(): String {
        return userLocalDatasource.getIdentity()
    }

    companion object {
        const val USER_ID = "user_id"
    }
}
