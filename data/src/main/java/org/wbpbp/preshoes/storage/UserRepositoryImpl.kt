/**
 * Copyright (C) 2020 WBPBP <potados99@gmail.com>
 *
 * This file is part of Preshoes (https://github.com/WBPBP).
 *
 * Preshoes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Preshoes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.wbpbp.preshoes.storage

import io.realm.Realm
import org.wbpbp.preshoes.entity.User
import org.wbpbp.preshoes.repository.UserRepository
import java.util.*

class UserRepositoryImpl : UserRepository {
    override fun saveUser(user: User) {
        Realm.getDefaultInstance().executeTransaction {
            it.copyToRealmOrUpdate(user.apply { email = email.toLowerCase(Locale.ROOT) })
        }
    }

    override fun deleteUser() {
        Realm.getDefaultInstance().executeTransaction {
            val user = it.where(User::class.java).findFirst()

            user?.deleteFromRealm()
        }
    }

    override fun getUser() = Realm.getDefaultInstance().where(User::class.java).findFirst()
}