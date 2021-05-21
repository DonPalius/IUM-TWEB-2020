<template>
  <div>
    <b-container class="bg-gray-200 mt-4 flex p-16">
      <b-row>
        <b-col cols="12">
          <calendar :courses="courses" />
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Calendar from '~/components/Calendar.vue'

export default {
  layout: 'logged',
  components: {
    Calendar
  },
  computed: {
    ...mapGetters(['user'])
  },
  async asyncData({ $axios }) {
    const { data } = await $axios.get('courses')
    return { courses: data }
  },
  mounted() {
  }
}
</script>
