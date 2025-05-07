<template>
  <div class="data-table-container mt-4">
    <h2>Список обучающихся</h2>

    <!-- Таблица обучающихся -->
    <table class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>ФИО</th>
        <th>Телефон</th>
        <th>Email</th>
        <th>Группа</th>
        <th>Дата регистрации</th>
        <th>Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="student in students" :key="student.studentId">
        <td>{{ student.studentId }}</td>
        <td>{{ student.studentName }}</td>
        <td>{{ student.phone }}</td>
        <td>{{ student.email }}</td>
        <td>{{ student.group?.groupName }}</td>
        <td>{{ formatDate(student.registrationDate) }}</td>
        <td>
          <button class="btn btn-link btn-sm text-warning me-2" @click="editStudent(student)" title="Редактировать">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button class="btn btn-link btn-sm text-danger" @click="deleteStudent(student.studentId)" title="Удалить">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Форма добавления/редактирования -->
    <div class="card mt-4">
      <div class="card-body">
        <h5 class="card-title">{{ isEditing ? 'Редактировать обучающегося' : 'Добавить обучающегося' }}</h5>
        <form @submit.prevent="saveStudent">
          <div class="mb-3">
            <label class="form-label">ФИО</label>
            <input v-model="form.studentName" type="text" class="form-control" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Телефон</label>
            <input v-model="form.phone" type="text" class="form-control" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input v-model="form.email" type="email" class="form-control">
          </div>
          <div class="mb-3">
            <label class="form-label">Группа</label>
            <select v-model="form.groupId" class="form-control" required>
              <option value="">Выберите группу</option>
              <option v-for="group in groups"
                      :key="group.groupId"
                      :value="group.groupId">
                {{ group.groupName }}
              </option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">Дата регистрации</label>
            <input v-model="form.registrationDate" type="date" class="form-control" required>
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
  name: 'StudentsList',
  setup() {
    const students = ref([])
    const groups = ref([])
    const isEditing = ref(false)
    const form = ref({
      studentName: '',
      phone: '',
      email: '',
      groupId: '',
      registrationDate: ''
    })
    const editingId = ref(null)

    const loadStudents = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/students')
        students.value = response.data
      } catch (error) {
        console.error('Error loading students:', error)
        alert('Ошибка при загрузке обучающихся')
      }
    }

    const loadGroups = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/groups')
        groups.value = response.data
      } catch (error) {
        console.error('Error loading groups:', error)
        alert('Ошибка при загрузке групп')
      }
    }

    const saveStudent = async () => {
      try {
        const studentData = {
          ...form.value,
          group: { groupId: form.value.groupId }
        }
        delete studentData.groupId

        if (isEditing.value) {
          await axios.put(`http://localhost:8080/api/students/${editingId.value}`, studentData)
        } else {
          await axios.post('http://localhost:8080/api/students', studentData)
        }
        await loadStudents()
        resetForm()
      } catch (error) {
        console.error('Error saving student:', error)
        alert('Ошибка при сохранении')
      }
    }

    const deleteStudent = async (id) => {
      if (confirm('Вы уверены, что хотите удалить этого обучающегося?')) {
        try {
          await axios.delete(`http://localhost:8080/api/students/${id}`)
          await loadStudents()
        } catch (error) {
          console.error('Error deleting student:', error)
          alert('Ошибка при удалении')
        }
      }
    }

    const editStudent = (student) => {
      form.value = {
        studentName: student.studentName,
        phone: student.phone,
        email: student.email,
        groupId: student.group?.groupId,
        registrationDate: student.registrationDate
      }
      editingId.value = student.studentId
      isEditing.value = true
    }

    const cancelEdit = () => {
      resetForm()
    }

    const resetForm = () => {
      form.value = {
        studentName: '',
        phone: '',
        email: '',
        groupId: '',
        registrationDate: ''
      }
      editingId.value = null
      isEditing.value = false
    }

    const formatDate = (date) => {
      return new Date(date).toLocaleDateString()
    }

    onMounted(() => {
      loadStudents()
      loadGroups()
    })

    return {
      students,
      groups,
      form,
      isEditing,
      saveStudent,
      deleteStudent,
      editStudent,
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
