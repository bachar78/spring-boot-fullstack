'use client'

import {
    Button,
    Flex,
    FormLabel,
    Heading,
    Input,
    Stack,
    Image, Box, Alert, AlertIcon, Link,
} from '@chakra-ui/react'
import {Formik, Form, useField} from "formik";
import * as Yup from 'yup';
import {useAuth} from "../context/AuthContext.jsx";
import {errorNotification} from "../../services/notification.js";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";


const MyTextInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status="error" mt={2}>
                    <AlertIcon/>
                    {meta.error}</Alert>
            ) : null}
        </Box>
    );
};


const LoginForm = () => {
    const {login} = useAuth();
    const navigate = useNavigate();
    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    email: Yup.string().email("Must be valid email").required("Email is required"),
                    password: Yup.string().max(20, "Password cannot be more than 20 characters").required("Password is required")
                })
            }
            initialValues={{email: '', password: ''}} onSubmit={(values, {setSubmitting}) => {
            // alert(JSON.stringify(values, null, 0))
            login(values).then(res => {
                setSubmitting(true)
                navigate("/dashboard")
                console.log("Success logged in");
            }).catch(err => {
                errorNotification(err.response.data.message)
            }).finally(() => {
                setSubmitting(false)
            })
        }}>
            {
                ({isValid, isSubmitting}) =>
                    (<Form>
                        <Stack spacing={12}>
                            <MyTextInput
                                label={"Email"}
                                name={"email"}
                                type={"email"}
                                placeholder={"Insert your email :)"}
                            />
                            <MyTextInput
                                label={"Password"}
                                name={"password"}
                                type={"password"}
                                placeholder={"Type your password"}
                            />
                            <Button
                                type={"submit"}
                                isDisabled={!isValid || isSubmitting}
                                bg={"teal"}

                            >Submit</Button>
                        </Stack>
                    </Form>)
            }
        </Formik>
    )
}

const Login = () => {

    const {customer} = useAuth();
    const navigate = useNavigate();
    useEffect(()=> {
        if(customer) {
            navigate("/dashboard")
        }
    })


    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Heading fontSize={'2xl'} mb={24}>Sign in to your account</Heading>
                    <LoginForm/>
                    <Link color={"blue.500"} href={"/signup"} mt={"18px"}> Don't have an account? SignUp</Link>
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

export default Login;