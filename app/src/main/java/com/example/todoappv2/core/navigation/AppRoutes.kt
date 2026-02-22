package com.example.todoappv2.core.navigation
object Routes {
    const val AUTH_GATE = "auth_gate"
    const val LOGIN = "login"
    const val LOGOUT ="logout"
    const val RESET_PASSWORD = "reset_password"

    const val REGISTER ="register"
    const val APP_SHELL ="app_shell"


    const val HOME = "home"
    const val SUBJECTS = "subjects"
    const val ADD_SUBJECT = "add_subject"
    const val EDIT_SUBJECT = "edit_subject/{subjectId}"

    fun editSubject(id: Long) = "edit_subject/$id"
    const val TASKS_BY_SUBJECT = "tasks/{subjectId}"
    fun tasksWithId(id: Long) = "tasks/$id"
    const val ADD_EDIT_TASK = "add_edit_tasks?taskId = {taskId}& subjectId = {subjectId}"
    fun addTask(subjectId: Long)="add_edit_task?subjectId =$subjectId"
    fun editTask(taskId:Long, subjectId: Long) = "edit_task_task?taskId=$taskId&subjectId=$subjectId"


    const val TASKS_ROOT = "tasks"
    const val STATS = "stats"
    const val NOTIFICATIONS = "notifications"
    const val SETTINGS = "settings"

}
