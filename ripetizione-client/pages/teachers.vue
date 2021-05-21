<template>
  <b-container>
    <b-button
      v-b-modal.modal-prevent-closing
      class="float-right"
      variant="success"
      size="sm"
    >Aggiungi Professore</b-button>
    <b-table striped hover borderless :fields="fields" :items="teachers">
      <template v-slot:cell(actions)="data">
        <b-button
          @click="deleteTeacher(data.item)"
          class="ml-auto"
          variant="danger"
          size="sm"
        >Elimina</b-button>
      </template>
    </b-table>

    <b-modal id="modal-prevent-closing" ref="modal" title="Aggiungi professore" @ok="handleOk">
      <form ref="form" @submit.stop.prevent="handleOk">
        <b-form-group label="Nome" label-for="name-input" invalid-feedback="Name is required">
          <b-form-input id="name-input" v-model="name" required></b-form-input>
        </b-form-group>
        <b-form-group
          label="Cognome"
          label-for="surname-input"
          invalid-feedback="Surname is required"
        >
          <b-form-input id="surname-input" v-model="surname" required></b-form-input>
        </b-form-group>
      </form>
    </b-modal>
  </b-container>
</template>
<style lang="css">
.pointer {
  cursor: pointer;
}
.card-cont {
  display: flex;
  flex-wrap: wrap;
}
.card {
  width: 30%;
  margin-bottom: 3rem;
  margin-right: 2rem;
}
</style>
<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  layout: 'logged',
  middleware: 'admin',
  async asyncData({ $axios }) {
    const { data } = await $axios.get('teachers')
    return { teachers: data }
  },
  data() {
    return {
      fields: [
        {
          key: 'name',
          sortable: true
        },
        'actions'
      ],
      name: '',
      surname: ''
    }
  },
  methods: {
    resetModal() {
      this.name = ''
      this.surname = ''
    },
    handleOk(bvModalEvt) {
      this.addTeacher(this.name, this.surname)
      this.$nextTick(() => {
        this.$refs.modal.hide()
        this.resetModal()
      })
    },
    async addTeacher(name, surname) {
      const { data } = await this.$axios.post('teachers', null, {
        params: {
          name: name,
          surname: surname
        }
      })
      this.teachers.push(data)
    },
    async deleteTeacher(t) {
      const id = t.id
      const { data } = await this.$axios.delete('teachers', {
        params: {
          id: id
        }
      })
      this.teachers = this.teachers.filter((t) => t.id != id)
    }
  }
}
</script>
