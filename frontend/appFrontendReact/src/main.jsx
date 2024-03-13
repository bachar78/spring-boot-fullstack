import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import {ChakraProvider} from '@chakra-ui/react'
import {ToastContainer} from 'react-toastify';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Login from "./components/login/Login.jsx";
import AuthProvider from "./components/context/AuthContext.jsx";
import ProtectedRoute from "./shared/ProtectedRoute.jsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Login/>
    }, {
        path: "dashboard",
        element: <ProtectedRoute><App/></ProtectedRoute>
    }
])

ReactDOM
    .createRoot(document.getElementById('root'))
    .render(
        <React.StrictMode>
            <ToastContainer/>
            <ChakraProvider>
                <AuthProvider>
                    <RouterProvider router={router}/>
                </AuthProvider>
            </ChakraProvider>
        </React.StrictMode>,
    )

