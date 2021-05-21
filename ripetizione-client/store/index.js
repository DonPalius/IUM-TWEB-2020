//On Login you get the token from backend, you save it, and use it for every call().
//Save session into the store, every call() get auth with token

export const state = () => ({
  user: null,
  loadingAvailablity: false,
  slots: [],
  selectedCourse: null,
  selectedTeacher: null,
  selectedSlot: null
})

export const getters = {
  user(state) {
    return state.user
  },
  isAdmin(state) {
    return state.user != null && state.user.is_admin
  },
  jwt() {
    return state.user != null ? state.user.token : null
  },
  selectedCourse(state) {
    return state.selectedCourse
  },
  selectedTeacher(state) {
    return state.selectedTeacher
  },
  selectedSlot(state) {
    return state.selectedSlot
  },
  slots(state) {
    return state.slots
  }
}

export const mutations = {
  SET_USER(state, u) {
    state.user = u
  },
  CLEAR_BOOKING(state) {
    state.selectedCourse = null
    state.selectedTeacher = null
    state.selectedSlot = null
  },
  SET_TEACHER(state, t) {
    state.selectedTeacher = t
  },
  SET_SLOT(state, s) {
    state.selectedSlot = s
  },
  SET_COURSE(state, t) {
    state.selectedCourse = t
  },
  SET_SLOTS(state, s) {
    state.slots = s.reduce((r, c) => {
      const d = r.find((o) => o.date == c.date)
      if (d != null) d.slots.push(c)
      else r.push({ date: c.date, slots: [c] })
      return r
    }, [])
  },
}

export const actions = {
  async setTeacher({ commit, state }, c) {
    commit('SET_TEACHER', c)
    commit('SET_SLOT', null)
    commit('SET_SLOTS', [])
    
    const { data } = await this.$axios.get('available_slots', {
      params: {
        teacher: c.id,
        course: state.selectedCourse.id
      }
    })
    commit('SET_SLOTS', data)
  },
  setCourse({ commit }, t) {
    commit('SET_COURSE', t)
    commit('SET_TEACHER', null)
    commit('SET_SLOT', null)
  },
  setSlot({ commit }, s) {
    commit('SET_SLOT', s)
  },
  async book({ commit, state }) {

    await this.$axios.post('bookings', null,{
      params: {
        course: state.selectedCourse.id,
        slot: state.selectedSlot.id
      }
    })

    commit('SET_COURSE', null)
    commit('SET_TEACHER', null)
    commit('SET_SLOT', null)

    this.$toast.show('Prenotazione effetuata')
  }
}
