package app.suprsend.android.user

import app.suprsend.android.config.ConfigHelper

internal class UserLocalDatasource : UserRepositoryContract {

    override fun identify(uniqueId: String) {
        ConfigHelper.addOrUpdate(UserRepository.USER_ID, uniqueId)
    }

    override fun getIdentity(): String {
        return ConfigHelper.get(UserRepository.USER_ID) ?: ""
    }

}