import api from './api';

export const authService = {
    login: async (email, password) => {
        const response = await api.post('/auth/login', { email, password });
        return response.data;
    },

    register: async (data) => {
        const response = await api.post('/students/register', data);
        return response.data;
    },
};

export default authService;
