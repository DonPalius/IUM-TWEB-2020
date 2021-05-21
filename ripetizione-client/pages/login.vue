<template>
  <div class="cont">
    <div>
      <b-card class="mb-3 p4">
        <b-form-group label="Username" label-for="input-email">
          <b-form-input
            id="input-email"
            v-model="form.username"
            type="text"
            required
            placeholder="me"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Password" label-for="input-password">
          <b-form-input
            @keyup.enter.native="login"
            id="input-password"
            v-model="form.password"
            type="password"
            required
            placeholder="**********"
          ></b-form-input>
        </b-form-group>

        <div class="d-flex mt-3 justify-content-between">
          <button class="btn btn-success" @click="login" type="button">Log In</button>
          <nuxt-link to="signup">
            <b-button variant="link">Signup</b-button>
          </nuxt-link>
        </div>
      </b-card>
      <div class="text-center mb-2 w-100">
        <nuxt-link to="/">Continua come Ospite</nuxt-link>
      </div>

      <p class="text-center text-muted">&copy;2020 Vue Ripetizioni Pierandrea e Riccardo.</p>
    </div>
  </div>
</template>
<style lang="css">
.cont {
  display: flex;
  justify-content: center;
  margin-top: 5rem;
}
.card {
  min-width: 300px;
}
</style>
<script>
export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    async login() {
      try {
        const { data } = await this.$axios.post('login', null, {
          params: this.form
        })
        this.$store.commit('SET_USER', data)
        this.$router.push('/')
      } catch (e) {}
    }
  }
}
</script>
