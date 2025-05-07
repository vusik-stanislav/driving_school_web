<template>
  <div class="data-table-container mt-4">
    <h2>Список инструкторов</h2>

    <!-- Таблица инструкторов -->
    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Телефон</th>
        <th>Дата найма</th>
        <th>Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="instructor in instructors" :key="instructor.instructorId">
        <td>{{ instructor.instructorId }}</td>
        <td>{{ instructor.instructorName }}</td>
        <td>{{ instructor.phone }}</td>
        <td>{{ formatDate(instructor.hireDate) }}</td>
        <td>
          <button class="btn btn-link btn-sm text-warning me-2" @click="editInstructor(instructor)" title="Редактировать">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button class="btn btn-link btn-sm text-danger" @click="deleteInstructor(instructor.instructorId)" title="Удалить">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Форма добавления/редактирования -->
    <div class="card mt-4">
      <div class="card-body">
        <h5 class="card-title">{{ isEditing ? 'Редактировать инструктора' : 'Добавить инструктора' }}</h5>
        <form @submit.prevent="saveInstructor">
          <div class="mb-3">
            <label class="form-label">Имя</label>
            <input v-model="form.instructorName" type="text" class="form-control" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Телефон</label>
            <input v-model="form.phone" type="text" class="form-control" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Дата найма</label>
            <input v-model="form.hireDate" type="date" class="form-control" required>
          </div>
          <button type="submit" class="btn btn-primary">{{ isEditing ? 'Обновить' : 'Добавить' }}</button>
          <button v-if="isEditing" type="button" class="btn btn-secondary ms-2" @click="cancelEdit">Отмена</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
// JavaScript код остается без изменений
import { ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'InstructorsList',
  setup() {
    const instructors = ref([])
    const isEditing = ref(false)
    const form = ref({
      instructorName: '',
      phone: '',
      hireDate: ''
    })
    const editingId = ref(null)

    const loadInstructors = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/instructors')
        instructors.value = response.data
      } catch (error) {
        console.error('Error loading instructors:', error)
        alert('Ошибка при загрузке данных')
      }
    }

    const saveInstructor = async () => {
      try {
        if (isEditing.value) {
          await axios.put(`http://localhost:8080/api/instructors/${editingId.value}`, form.value)
        } else {
          await axios.post('http://localhost:8080/api/instructors', form.value)
        }
        await loadInstructors()
        resetForm()
      } catch (error) {
        console.error('Error saving instructor:', error)
        alert('Ошибка при сохранении')
      }
    }

    const deleteInstructor = async (id) => {
      if (confirm('Вы уверены, что хотите удалить этого инструктора?')) {
        try {
          await axios.delete(`http://localhost:8080/api/instructors/${id}`)
          await loadInstructors()
        } catch (error) {
          console.error('Error deleting instructor:', error)
          alert('Ошибка при удалении')
        }
      }
    }

    const editInstructor = (instructor) => {
      form.value = {
        instructorName: instructor.instructorName,
        phone: instructor.phone,
        hireDate: instructor.hireDate
      }
      editingId.value = instructor.instructorId
      isEditing.value = true
    }

    const cancelEdit = () => {
      resetForm()
    }

    const resetForm = () => {
      form.value = {
        instructorName: '',
        phone: '',
        hireDate: ''
      }
      editingId.value = null
      isEditing.value = false
    }

    const formatDate = (date) => {
      return new Date(date).toLocaleDateString()
    }

    onMounted(loadInstructors)

    return {
      instructors,
      form,
      isEditing,
      saveInstructor,
      deleteInstructor,
      editInstructor,
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
