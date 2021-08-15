package app.suprsend.android.user

import app.suprsend.android.base.SSConstants
import app.suprsend.android.config.ConfigHelper

internal class UserLocalDatasource : UserRepositoryContract {

    override fun identify(uniqueId: String) {
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_USER_ID, uniqueId)
    }

    override fun getIdentity(): String {
        return ConfigHelper.get(SSConstants.CONFIG_USER_ID) ?: ""
    }
}
