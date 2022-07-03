import Index from '../pages/Index.vue'
import CreateTask from '../pages/CreateTask.vue'
import Analysis from '../pages/Analysis.vue'

const routes = [
    { path: '/', component: Index },
    { path: '/task/create', component: CreateTask },
    { path: '/analysis', component: Analysis },
]

export default routes;