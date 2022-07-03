import axios, {AxiosInstance} from 'axios';

const myAxios : AxiosInstance = axios.create({
    baseURL: 'http://localhost:7529/api/',
    timeout: 5000,
    headers: {'X-Custom-Header': 'foobar'}
});

// Add a request interceptor
myAxios.interceptors.request.use(function (config) {
    console.log('request', config);
    // Do something before request is sent
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});

// Add a response interceptor
myAxios.interceptors.response.use(function (response) {
    console.log('response', response);
    // Do something with response data
    return response.data;
}, function (error) {
    // Do something with response error
    return Promise.reject(error);
});

export default myAxios;