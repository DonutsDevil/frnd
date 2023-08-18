package com.swapnil.frnd.repository

import com.swapnil.frnd.repository.local.service.TaskLocalService
import com.swapnil.frnd.repository.network.service.TaskNetworkService
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskNetworkService: TaskNetworkService,
    private val taskLocalService: TaskLocalService
) {

}