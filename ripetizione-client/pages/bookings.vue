<template>
  <b-container class="mt-3">
    <b-table striped hover borderless :fields="fields" :items="bookings">
      <template v-slot:cell(status)="data">
        <b v-if="data.value == 10" class>Creata</b>
        <b v-if="data.value == 20" class="text-danger">Annullata</b>
        <b v-if="data.value == 30" class="text-success">Confermata</b>
      </template>

      <template v-slot:cell(actions)="data">
        <b-button
          v-if="data.item.canCancel"
          @click="cancelBooking(data.item)"
          class="ml-auto"
          variant="danger"
          size="sm"
        >Annulla</b-button>
        <b-button
          v-if="(isAdmin && user.username == data.item.user && data.item.canConfirm) || (!isAdmin && data.item.canConfirm)"
          @click="confirmBooking(data.item)"
          class="ml-auto"
          variant="success"
          size="sm"
        >Conferma</b-button>
      </template>
    </b-table>
  </b-container>
</template>

<script>
import { mapGetters } from 'vuex'
import * as moment from 'moment'

export default {
  layout: 'logged',
  middleware: 'logged',

  data() {
    return {
      fields: [
        {
          key: 'user',
          sortable: true
        },
        {
          key: 'course',
          sortable: true
        },
        {
          key: 'teacher',
          sortable: true
        },

        {
          key: 'date',
          sortable: true
        },
        {
          key: 'start',
          sortable: false
        },
        'status',
        'actions'
      ]
    }
  },
  methods: {
    async cancelBooking(b) {
      await this.$axios.delete('bookings', {
        params: {
          id: b.id
        }
      })

      this.bookings = this.bookings.map((a) => {
        if (b.id == a.id) {
          a.status = 20
          a.canCancel = false
        }

        return a
      })
    },
    async confirmBooking(b) {
      await this.$axios.put('bookings', null, {
        params: {
          id: b.id
        }
      })

      this.bookings = this.bookings.map((a) => {
        if (b.id == a.id) {
          a.status = 30
          a.canConfirm = false
        }

        return a
      })
    }
  },
  computed: {
    ...mapGetters(['isAdmin', 'user'])
  },

  async asyncData({ $axios }) {
    const { data } = await $axios.get('bookings')

    const now = moment()

    const l = data.map((b) => {
      const t = moment(`${b.date} ${b.start}`, 'YYYY-MM-DD HH:mm')

      b['canCancel'] = b.status == 10 && now.isBefore(t)
      b['canConfirm'] = b.status == 10 && now.isAfter(t)
      return b
    })

    return { bookings: l }
  }
}
</script>
