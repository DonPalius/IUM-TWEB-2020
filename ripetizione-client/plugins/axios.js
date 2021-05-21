export default ({ $axios, app, store, redirect, ...others }) => {
  $axios.onError((error) => {

    if (error.response && error.response.data) {
      const data = error.response.data
      app.$toast.error(data.error)
    } else {
      app.$toast.error(error)
    }

    if (error.response && error.response.status === 401) {
      store.commit("SET_USER", null)
      window.location = "/login"
    }

  })

  $axios.onRequest((config) => {
    const u = store.state.user
    if (u != null && u.token != null) {
      config.params = { ...config.params, token: u.token }
    }
  })
}
