<template>
  <b-container>
    <b-button v-b-modal.modal-prevent-closing variant="success">Aggiungi corso</b-button>
    <div v-for="(c, i) of courses" :key="c.id">
      <card-course :teachers="teachers" v-model="courses[i]" class="mb-3"></card-course>
    </div>


    <b-modal id="modal-prevent-closing" ref="modal" title="Aggiungi Corso" @ok="handleOk">
      <form ref="form" @submit.stop.prevent="handleOk">
        <b-form-group label="Nome" label-for="name-input" invalid-feedback="Name is required">
          <b-form-input id="name-input" v-model="name" required></b-form-input>
        </b-form-group>
      </form>
    </b-modal>
  </b-container>
</template>
<script>
import CardCourse from '~/components/CardCourse'
import { mapGetters } from 'vuex'

export default {
  layout: 'logged',
  middleware: 'admin',
  components: { CardCourse },
  data() {
    return {
      name: ''
    }
  },
  methods: {
    resetModal() {
      this.name = ''
    },
    handleOk(bvModalEvt) {
      this.addCourse(this.name)
      this.$nextTick(() => {
        this.$refs.modal.hide()
        this.resetModal()
      })
    },
    async addCourse(name, surname) {
      const { data } = await this.$axios.post('courses', null, {
        params: {
          name: name
        }
      })
      this.courses.push(data)
    }
  },

  async asyncData({ $axios }) {
    const { data } = await $axios.get('courses')
    const t = (await $axios.get('teachers')).data
    return {
      courses: data,
      teachers: t
    }
  }
}
</script>
