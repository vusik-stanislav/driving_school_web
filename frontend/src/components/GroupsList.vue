<template>
  <div class="container-fluid mt-4">
    <h2>Список групп</h2>

    <!-- Таблица групп -->
    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Дата начала</th>
        <th>Дата окончания</th>
        <th>Инструктор</th>
        <th>Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="group in groups" :key="group.groupId">
        <td>{{ group.groupId }}</td>
        <td>{{ group.groupName }}</td>
        <td>{{ formatDate(group.startDate) }}</td>
        <td>{{ formatDate(group.endDate) }}</td>
        <td>{{ group.instructor?.instructorName }}</td>
        <td>
          <button class="btn btn-link btn-sm text-warning me-2" @click="editGroup(group)" title="Редактировать">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button class="btn btn-link btn-sm text-danger" @click="deleteGroup(group.groupId)" title="Удалить">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Форма добавления/редактирования -->
    <div class="card mt-4">
      <div class="card-body">
        <h5 class="card-title">{{ isEditing ? 'Редактировать группу' : 'Добавить группу' }}</h5>
        <form @submit.prevent="saveGroup">
          <div class="mb-3">
            <label class="form-label">Название группы</label>
            <input v-model="form.groupName" type="text" class="form-control" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Дата начала</label>
            <input v-model="form.startDate" type="date" class="form-control" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Дата окончания</label>
            <input v-model="form.endDate" type="date" class="form-control" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Инструктор</label>
            <select v-model="form.instructorId" class="form-control" required>
              <option value="">Выберите инструктора</option>
              <option v-for="instructor in instructors"
                      :key="instructor.instructorId"
                      :value="instructor.instructorId">
                {{ instructor.instructorName }}
              </option>
            </select>
          </div>
          <button type="submit" class="btn btn-primary">{{ isEditing ? 'Обновить' : 'Добавить' }}</button>
          <button v-if="isEditing" type="button" class="btn btn-secondary ms-2" @click="cancelEdit">Отмена</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'GroupsList',
  setup() {
    const groups = ref([])
    const instructors = ref([])
    const isEditing = ref(false)
    const form = ref({
      groupName: '',
      startDate: '',
      endDate: '',
      instructorId: ''
    })
    const editingId = ref(null)

    const loadGroups = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/groups')
        groups.value = response.data
      } catch (error) {
        console.error('Error loading groups:', error)
        alert('Ошибка при загрузке групп')
      }
    }

    const loadInstructors = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/instructors')
        instructors.value = response.data
      } catch (error) {
        console.error('Error loading instructors:', error)
        alert('Ошибка при загрузке инструкторов')
      }
    }

    const saveGroup = async () => {
      try {
        const groupData = {
          ...form.value,
          instructor: { instructorId: form.value.instructorId }
        }
        delete groupData.instructorId

        if (isEditing.value) {
          await axios.put(`http://localhost:8080/api/groups/${editingId.value}`, groupData)
        } else {
          await axios.post('http://localhost:8080/api/groups', groupData)
        }
        await loadGroups()
        resetForm()
      } catch (error) {
        console.error('Error saving group:', error)
        alert('Ошибка при сохранении')
      }
    }

    const deleteGroup = async (id) => {
      if (confirm('Вы уверены, что хотите удалить эту группу?')) {
        try {
          await axios.delete(`http://localhost:8080/api/groups/${id}`)
          await loadGroups()
        } catch (error) {
          console.error('Error deleting group:', error)
          alert('Ошибка при удалении')
        }
      }
    }

    const editGroup = (group) => {
      form.value = {
        groupName: group.groupName,
        startDate: group.startDate,
        endDate: group.endDate,
        instructorId: group.instructor?.instructorId
      }
      editingId.value = group.groupId
      isEditing.value = true
    }

    const cancelEdit = () => {
      resetForm()
    }

    const resetForm = () => {
      form.value = {
        groupName: '',
        startDate: '',
        endDate: '',
        instructorId: ''
      }
      editingId.value = null
      isEditing.value = false
    }

    const formatDate = (date) => {
      return new Date(date).toLocaleDateString()
    }

    onMounted(() => {
      loadGroups()
      loadInstructors()
    })

    return {
      groups,
      instructors,
      form,
      isEditing,
      saveGroup,
      deleteGroup,
      editGroup,
      cancelEdit,
      formatDate
    }
  }
}
</script>

<style>
.btn-link:hover {
  opacity: 0.7;
}
</style>
