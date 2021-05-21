import VuexPersistence from 'vuex-persist'

import SecureLS from "secure-ls";
var ls = new SecureLS();

//Save Data into Storage
export default ({ store }) => {
  new VuexPersistence({
    storage: ls,
    restoreState: (key, storage) => storage.get(key),
    saveState: (key, state, storage) => storage.set(key, state)
  }).plugin(store)
}
