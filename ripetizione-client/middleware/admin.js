export default ({ store, route, redirect }) => {
  if (!store.getters.isAdmin) {
    redirect('/')
  }
}
