import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter, DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const AddIcon = () => "+";
const CloseIcon = () => "x";
const DrawerForm = ({fetchCustomers}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button leftIcon={<AddIcon/>}
                    onClick={onOpen}
                    colorScheme={"teal"}
            >
                Add Customer
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create New Customer</DrawerHeader>
                    <DrawerBody>
                        <CreateCustomerForm fetchCustomers={fetchCustomers}  onClose={onClose}/>
                    </DrawerBody>
                    <DrawerFooter>
                        <Button
                            leftIcon={<CloseIcon/>}
                            onClick={onClose} form='my-form'>
                          Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}

export default DrawerForm;