package app.suprsend.android.user

import app.suprsend.android.SSApiInternal
import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.config.ConfigHelper

internal class UserLocalDatasource : UserRepositoryContract {

    override fun identify(uniqueId: String) {
        Logger.i(TAG, "Identity : $uniqueId")
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_USER_ID, uniqueId)
    }

    override fun getIdentity(): String {
        return ConfigHelper.get(SSConstants.CONFIG_USER_ID) ?: ""
    }

    companion object {
        const val TAG = SSApiInternal.TAG
    }
}
