<template>
  <div class="manage">
    <div class="toolbar">
      <el-button type="primary" @click="openAdd">新增去处</el-button>
    </div>
    <el-table :data="list" border stripe style="margin-top:16px" v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="名称" min-width="140" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="region" label="区域" width="100" />
      <el-table-column prop="heatIndex" label="热度" width="80" />
      <el-table-column prop="avgCost" label="人均" width="90" />
      <el-table-column prop="address" label="地址" min-width="160" show-overflow-tooltip />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑去处' : '新增去处'" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" style="width:100%">
            <el-option label="景点" value="attraction" />
            <el-option label="美食" value="food" />
            <el-option label="活动" value="activity" />
          </el-select>
        </el-form-item>
        <el-form-item label="区域" prop="region">
          <el-input v-model="form.region" />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="form.tags" placeholder="逗号分隔" />
        </el-form-item>
        <el-form-item label="封面图">
          <el-input v-model="form.cover" />
        </el-form-item>
        <el-form-item label="热度">
          <el-input-number v-model="form.heatIndex" :min="0" />
        </el-form-item>
        <el-form-item label="人均">
          <el-input-number v-model="form.avgCost" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="开放时间">
          <el-input v-model="form.openTime" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.intro" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminSpotList, addAdminSpot, updateAdminSpot, deleteAdminSpot } from '../../api'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = ref({})
const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
}

const loadList = async () => {
  loading.value = true
  list.value = await getAdminSpotList()
  loading.value = false
}

const openAdd = () => {
  isEdit.value = false
  form.value = { type: 'attraction', heatIndex: 0, avgCost: 0 }
  dialogVisible.value = true
}

const openEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate(async valid => {
    if (!valid) return
    try {
      if (isEdit.value) {
        await updateAdminSpot(form.value.id, form.value)
      } else {
        await addAdminSpot(form.value)
      }
      ElMessage.success('操作成功')
      dialogVisible.value = false
      loadList()
    } catch (e) {}
  })
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning' })
  await deleteAdminSpot(row.id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>

<style scoped>
.manage { padding: 0; }
.toolbar { display: flex; justify-content: flex-end; }
</style>
