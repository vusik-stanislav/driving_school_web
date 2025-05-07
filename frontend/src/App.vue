<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <span class="navbar-brand">Автошкола</span>
        <div class="navbar-nav">
          <a class="nav-link" href="#"
             :class="{ active: currentView === 'instructors' }"
             @click.prevent="currentView = 'instructors'">
            Инструкторы
          </a>
          <a class="nav-link" href="#"
             :class="{ active: currentView === 'groups' }"
             @click.prevent="currentView = 'groups'">
            Группы
          </a>
          <a class="nav-link" href="#"
             :class="{ active: currentView === 'students' }"
             @click.prevent="currentView = 'students'">
            Обучающиеся
          </a>
        </div>
      </div>
    </nav>

    <div class="content-wrapper">
      <InstructorsList v-if="currentView === 'instructors'" />
      <GroupsList v-if="currentView === 'groups'" />
      <StudentsList v-if="currentView === 'students'" />
    </div>

  </div>
</template>

<script>
import { ref } from 'vue'
import InstructorsList from './components/InstructorsList.vue'
import GroupsList from './components/GroupsList.vue'
import StudentsList from './components/StudentsList.vue'

export default {
  name: 'App',
  components: {
    InstructorsList,
    GroupsList,
    StudentsList
  },
  setup() {
    const currentView = ref('instructors')

    return {
      currentView
    }
  }
}
</script>

<style>
.content-wrapper {
  max-width: 1400px; /* или другое желаемое значение */
  width: 100%;
  margin: 0 auto;
  padding: 0 20px;
  }

.data-table-container {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

</style>
