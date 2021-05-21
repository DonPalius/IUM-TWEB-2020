<template>
  <b-button
    @click="$emit('click')"
    :disabled="disabled"
    :variant="value ? 'success' : 'dark'"
    class=" font-bold py-2 px-4 "
  >
    <slot></slot>
  </b-button>
</template>
<script>
export default {
  props: {
    value: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    }
  }
}
</script>
