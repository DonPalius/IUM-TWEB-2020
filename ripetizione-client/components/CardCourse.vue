<template>
  <b-card class="w-100">
    <div class="d-flex mb-3">
      <h5>
        <b>{{value.name}}</b>
      </h5>
      <b-form-select v-model="selected" :options="availableTeachers" class="fixed-w ml-auto mr-3">
        <!-- This slot appears above the options from 'options' prop -->
        <template v-slot:first>
          <option :value="null" disabled>Scegli un professore</option>
        </template>
      </b-form-select>
      <b-button @click="addTeacher" class variant="success">Aggiungi</b-button>
    </div>
    <div class="d-flex">
      <b-button-group v-for="t of value.teachers" :key="t.id" class="mr-2">
        <b-button>{{ t.name }}</b-button>
        <b-button @click="removeTeacher(t.id)" variant="danger">
          <fa icon="trash-alt" />
        </b-button>
      </b-button-group>
    </div>
  </b-card>
</template>
<style>
.fixed-w {
  max-width: 15rem;
}
</style>
<script>
export default {
  props: {
    value: {
      type: Object,
      required: true
    },
    teachers: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      selected: null,
      options: {}
    }
  },
  methods: {
    async addTeacher() {
      if (this.selected != null) {
        const { data } = await this.$axios.post('course_teacher', null, {
          params: {
            course: this.value.id,
            teacher: this.selected
          }
        })

        this.selected = null
        this.$emit('input', data)
      }
    },
    async removeTeacher(teacherId) {
      await this.$axios.delete('course_teacher', {
        params: {
          course: this.value.id,
          teacher: teacherId
        }
      })

      this.$emit('input', {
        ...this.value,
        teachers: this.value.teachers.filter((t) => t.id != teacherId)
      })
    }
  },
  computed: {
    availableTeachers() {
      return this.teachers
        .filter((t) => this.value.teachers.find((a) => a.id === t.id) == null)
        .map((t) => {
          return { value: t.id, text: t.name }
        })
    }
  }
}
</script>
