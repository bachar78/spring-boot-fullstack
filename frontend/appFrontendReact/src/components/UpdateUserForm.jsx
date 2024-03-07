import {Formik, Form, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Stack} from "@chakra-ui/react";
import { updateCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";

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

const UpdateUserForm = ({fetchCustomers, initialValues, customerId}) => {
    const {name, email, age, gender} = initialValues;
    return (
        <>
            <Formik
                initialValues={initialValues}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less'),
                    email: Yup.string()
                        .email('Invalid email address'),
                    age: Yup.number()
                        .min(16, 'You must be at least 16 years of age')
                        .max(100, 'Less than 100 years of age')
                })}
                onSubmit={(updatedCustomer, {setSubmitting}) => {
                    setSubmitting(true)
                    updateCustomer(updatedCustomer,customerId).then(res => {
                        fetchCustomers()
                        successNotification(`${updatedCustomer.name} was successfully updated`)
                    }).catch(err => {
                        errorNotification(err.response.data.message)
                    }).finally(() => {
                        setSubmitting(false)
                    })
                }}
            >
                {({isValid, isSubmitting, dirty}) => (
                    <Form>
                        <Stack spacing={"24px"}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="Insert your name"
                            />
                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="Insert your email"
                            />
                            <MyTextInput
                                label="Age"
                                name="age"
                                type="number"
                                placeholder="Insert your age"
                            />
                            <Button
                                isDisabled={!(dirty && isValid) || isSubmitting}
                                type="submit"
                                isLoading={isSubmitting}
                                loadingText={"Submitting"}
                            >Submit</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default UpdateUserForm;