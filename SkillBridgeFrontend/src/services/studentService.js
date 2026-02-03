import api from './api';

export const studentService = {
    getProfile: async () => {
        const response = await api.get('/students/info');
        return response.data;
    },

    getById: async (id) => {
        const response = await api.get(`/students/${id}`);
        return response.data;
    },

    getAll: async () => {
        const response = await api.get('/students/all');
        return response.data;
    },

    updateProfile: async (data) => {
        const response = await api.put('/students/update', data);
        return response.data;
    },

    patchProfile: async (data) => {
        const response = await api.patch('/students/patch', data);
        return response.data;
    },
};

export default studentService;
