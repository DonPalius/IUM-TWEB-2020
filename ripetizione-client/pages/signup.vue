<template>
  <div class="cont">
    <div>
      <b-card class="mb-4 p4">
        <b-form-group label="Username" label-for="input-email">
          <b-form-input
            id="input-email"
            v-model="form.username"
            type="text"
            required
            placeholder="me@unito.it"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Password" label-for="input-password">
          <b-form-input
            id="input-passwrod"
            v-model="form.password"
            type="password"
            required
            placeholder="**********"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Conferma password" label-for="input-confirm-password">
          <b-form-input
            id="input-email"
            v-model="form.password"
            type="password"
            required
            placeholder="**********"
          ></b-form-input>
        </b-form-group>

        <div class="d-flex mt-3 justify-content-between">
          <button class="btn btn-secondary" type="button">Signup</button>
          <nuxt-link to="login">
            <b-button variant="link">Login</b-button>
          </nuxt-link>
        </div>
      </b-card>
      <p class="text-center text-muted">&copy;2020 Vue Ripetizioni. All rights reserved.</p>
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
        password: '',
        consfirmPassword: ''
      }
    }
  }
}
</script>