import { defineStore } from 'pinia'

export const useSelectedStore = defineStore('selected', {
  state: () => ({
    spotIds: [],
    spotNames: []
  }),
  actions: {
    addSpot(id, name) {
      if (!this.spotIds.includes(id)) {
        this.spotIds.push(id)
        this.spotNames.push(name)
      }
    },
    removeSpot(id) {
      const idx = this.spotIds.indexOf(id)
      if (idx > -1) {
        this.spotIds.splice(idx, 1)
        this.spotNames.splice(idx, 1)
      }
    },
    clear() {
      this.spotIds = []
      this.spotNames = []
    }
  }
})
