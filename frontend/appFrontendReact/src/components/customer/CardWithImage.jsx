'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue,
    Button,
    useDisclosure,
    AlertDialog,
    AlertDialogOverlay,
    AlertDialogContent,
    AlertDialogHeader,
    AlertDialogBody, AlertDialogFooter,
} from '@chakra-ui/react'
import {useRef} from "react";
import {deleteCustomer, getCustomers} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";
import UpdateUserDrawer from "./UpdateUserDrawer.jsx";
import {jwtDecode} from "jwt-decode"
import {useAuth} from "../context/AuthContext.jsx";

export default function CardWithImage({id, name, email, age, gender, imageNumber, fetchCustomers}) {
    const randomUserGender = gender === "MALE" ? "men" : "women";
    const {isOpen, onOpen, onClose} = useDisclosure()
    const cancelRef = useRef()
    const {logout} = useAuth();
    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                minW={'300px'}
                w={'full'}
                m={2}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'lg'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit="cover"
                    alt="#"
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            `https://randomuser.me/api/portraits/${randomUserGender}/${imageNumber}.jpg`
                        }
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box>
                    <Stack spacing={2} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>{id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text color={'gray.500'}>Age {age} | {gender} </Text>
                    </Stack>
                </Box>
                <Stack m={8} mt={12} direction={"row"} align={'center'} justify={'center'}>
                    <Button
                        bg={'red.400'}
                        color={"white"}
                        rounded={'full'}
                        _hover={
                            {
                                transform: 'translateY(-2px)',
                                boxShadow: 'lg'
                            }
                        }
                        _focus={{
                            bg: 'green.500'

                        }}
                        onClick={onOpen}
                    >
                        Delete
                    </Button>
                    <AlertDialog
                        isOpen={isOpen}
                        leastDestructiveRef={cancelRef}
                        onClose={onClose}
                    >
                        <AlertDialogOverlay>
                            <AlertDialogContent>
                                <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                                    Delete Customer
                                </AlertDialogHeader>

                                <AlertDialogBody>
                                    Are you sure you want to delete {name}? You can't undo this action afterwards.
                                </AlertDialogBody>

                                <AlertDialogFooter>
                                    <Button ref={cancelRef} onClick={onClose}>
                                        Cancel
                                    </Button>
                                    <Button colorScheme='red' onClick={() => {
                                        deleteCustomer(id).then(res => {
                                            console.log(res)
                                            fetchCustomers()
                                            successNotification(`${name} was successfully deleted`)
                                            const token = localStorage.getItem("access_token")
                                            if (token) {
                                                const decodedToken = jwtDecode(token)
                                                if (decodedToken.sub === email) {
                                                    logout()
                                                }

                                            }
                                        }).catch(err => {
                                            console.log(err)
                                            errorNotification(err.response.data.message)
                                        }).finally(() => onClose())
                                    }}
                                            ml={3}>
                                        Delete
                                    </Button>
                                </AlertDialogFooter>
                            </AlertDialogContent>
                        </AlertDialogOverlay>
                    </AlertDialog>
                    <UpdateUserDrawer
                        fetchCustomers={fetchCustomers}
                        initialValues={{name, email, age, gender}}
                        customerId={id}
                    />
                </Stack>
            </Box>
        </Center>
    )
}