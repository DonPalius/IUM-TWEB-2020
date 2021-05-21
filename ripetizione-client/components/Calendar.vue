<template>
  <div class="block">
    <h4 class="mb-1">Corso</h4>
    <div class="inline-flex">
      <toggle-button
        v-for="c of courses"
        @click="setCourse(c)"
        class="mr-1"
        :key="c.id"
        :value="selectedCourse && selectedCourse.id == c.id"
      >{{ c.name }}</toggle-button>
    </div>

    <h4 class="mt-3 mb-1">Professore</h4>
    <div v-if="selectedCourse" class="inline-flex">
      <toggle-button
        v-for="t of selectedCourse.teachers"
        @click="setTeacher(t)"
        class="mr-1"
        :key="t.id"
        :value="selectedTeacher && selectedTeacher.id == t.id"
      >{{ t.name }}</toggle-button>
    </div>

    <div v-else class="inline-flex">
      <toggle-button v-for="t of [0, 1, 2, 3]" class="mr-1" :disabled="true" :key="t">...</toggle-button>
    </div>

    <div v-if="selectedTeacher">
      <h4 class="my-3">Disponibilita</h4>
      <div class="d-flex">
        <div v-for="s of slots" :key="s.date" class="d-flex flex-column mr-2">
          <h6 class="px-2 pb-1 mb-2 border-bottom border-dark text-center">
            <b>{{ $moment(s.date, 'YYYY-MM-DD').format('dd DD MMM YYYY') }}</b>
          </h6>
          <toggle-button
            v-for="t of s.slots"
            @click="setSlot(t)"
            :value="selectedSlot && selectedSlot.id == t.id"
            class="mb-1"
            :key="t.id"
          >{{ $moment(t.start, 'HH:mm:ss').format('HH:mm') }}</toggle-button>
        </div>
      </div>
    </div>
    <hr />

    <div class="mt-2">
      <b-button
        v-if="user"
        :disabled="selectedSlot == null"
        variant="success"
        @click="book()"
        class="py-2 px-4"
      >Prenota</b-button>

      <nuxt-link v-else to="/login">
        <b-button variant="link" class="py-2 px-4">Effettua il login per prenotarti</b-button>
      </nuxt-link>
    </div>
  </div>
</template>

<script>

import ToggleButton from '~/components/ToggleButton.vue'
import { mapGetters } from 'vuex'
import { mapActions } from 'vuex'

export default {
  props: ['courses'],
  components: {
    ToggleButton
  },
  computed: {
    ...mapGetters([
      'selectedCourse',
      'selectedTeacher',
      'selectedSlot',
      'slots',
      'user'
    ])
  },
  methods: {
    ...mapActions(['setTeacher', 'setCourse', 'setSlot', 'book'])
  }
}
</script>
