import {Wrap, WrapItem, Spinner, Text, useDisclosure} from '@chakra-ui/react'
import SidebarWithHeader from "./shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/CardWithImage.jsx";
import DrawerForm from "./components/DrawerForm.jsx";
import 'react-toastify/dist/ReactToastify.css';
import {errorNotification} from "./services/notification.js";



const App = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchCustomers = () => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data);
        }).catch(err => {
            setError(err.response.data.message)
            errorNotification(err.response.data.message)
        }).finally(() => {
            setLoading(false);
        })
    }


    useEffect(() => {
       fetchCustomers()
    }, []);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }
    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <DrawerForm/>
                <Text mt={8}>No Customers Available</Text>
            </SidebarWithHeader>
        )
    }
    if (err !== "") {
        return (
            <SidebarWithHeader>
                <DrawerForm/>
                <Text mt={8}>Ooooops there was an error</Text>
            </SidebarWithHeader>
        )
    }
    return (
        <SidebarWithHeader>
            <DrawerForm fetchCustomers={fetchCustomers}/>
            <Wrap justify={"center"} spacing={"30px"}>
                {
                    customers.map((customer, index) => (
                        <WrapItem key={index}>
                            <CardWithImage
                                {...customer}
                                imageNumber={index}
                                fetchCustomers={fetchCustomers}
                            />
                        </WrapItem>
                    ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App