/*
 * Copyright (c) 2020 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.app.features.userdirectory

import androidx.paging.PagedList
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import im.vector.app.core.contacts.MappedContact
import org.matrix.android.sdk.api.session.user.model.User

data class UserListViewState(
        val excludedUserIds: Set<String>? = null,
        val knownUsers: Async<PagedList<User>> = Uninitialized,
        val directoryUsers: Async<List<User>> = Uninitialized,
        val mappedContacts: Async<List<MappedContact>> = Loading(),
        // Tru to display only bound contacts with their bound 2pid
        val onlyBoundContacts: Boolean = false,
        // All contacts, filtered by searchTerm and onlyBoundContacts
        val filteredMappedContacts: List<MappedContact> = emptyList(),
        // True when the identity service has return some data
        val isBoundRetrieved: Boolean = false,
        val pendingInvitees: Set<PendingInvitee> = emptySet(),
        val createAndInviteState: Async<String> = Uninitialized,
        val searchTerm: String = ""
) : MvRxState {

    fun getSelectedMatrixId(): List<String> {
        return pendingInvitees
                .mapNotNull {
                    when (it) {
                        is PendingInvitee.UserPendingInvitee     -> it.user.userId
                        is PendingInvitee.ThreePidPendingInvitee -> null
                    }
                }
    }
}
