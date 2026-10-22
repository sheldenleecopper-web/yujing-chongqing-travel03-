<template>
  <div class="manage">
    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" min-width="140" />
      <el-table-column prop="role" label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'">{{ row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" min-width="180" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminUserList } from '../../api'

const list = ref([])
const loading = ref(false)

const loadList = async () => {
  loading.value = true
  list.value = await getAdminUserList()
  loading.value = false
}

onMounted(loadList)
</script>

<style scoped>
.manage { padding: 0; }
</style>
