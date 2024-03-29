import {Form, Formik, useField} from "formik";
import {
    Alert,
    AlertIcon,
    Box,
    Button,
    Flex,
    FormLabel,
    Heading,
    Image,
    Input,
    Link,
    Select,
    Stack
} from "@chakra-ui/react";
import * as Yup from "yup";
import {saveCustomer} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";
import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import CreateCustomerForm from "../customer/CreateCustomerForm.jsx";

const Signup = () => {
    const {customer, setCustomerFromToken} = useAuth();
    const navigate = useNavigate();
    useEffect(()=> {
        const jwt = localStorage.getItem("access_token")
        if(jwt) {
            navigate("/dashboard")
        }
    })

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Heading fontSize={'2xl'} mb={24}>Register for an Account</Heading>
                    <CreateCustomerForm onSuccess={(token)=> {
                        localStorage.setItem("access_token", token)
                        setCustomerFromToken(token)
                        navigate("/dashboard")
                    }}/>
                    <Link color={"blue.500"} href={"/"} mt={"18px"}> Already have an account? Login now</Link>
                </Stack>
            </Flex>
            <Flex flex={1}>
                <Image
                    alt={'Login Image'}
                    objectFit={'cover'}
                    src={
                        'https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1352&q=80'
                    }
                />
            </Flex>
        </Stack>
    )

}


export default Signup;