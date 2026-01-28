package com.example.todoappv2.task.add_edit

sealed class TaskAddEditEvent {
    data class TitleChanged(val value: String): TaskAddEditEvent()
    data class DescriptionChanged(val value: String):TaskAddEditEvent()
    data class DueDateChanged(val value: Long?):TaskAddEditEvent()
    data class CompletionToggled(val value: Boolean): TaskAddEditEvent()

    object SaveTask : TaskAddEditEvent()

}