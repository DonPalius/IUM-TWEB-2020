export default ({ store, route, redirect }) => {
  if (store.getters.user == null) {
    redirect('/')
  }
}
