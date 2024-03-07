import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter, DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";

import UpdateUserForm from "./UpdateUserForm.jsx";

const AddIcon = () => "+";
const CloseIcon = () => "x";
const UpdateUserDrawer = ({fetchCustomers, initialValues, customerId}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button
                    onClick={onOpen}
                    bg={"gray.400"}
                    color={"black"}
                    rounded={'full'}
                    _hover={
                        {
                            transform: 'translateY(-2px)',
                            boxShadow: 'lg'
                        }
                    }
            >
                Update
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Update {initialValues.name}</DrawerHeader>
                    <DrawerBody>
                        <UpdateUserForm initialValues={initialValues} customerId={customerId} fetchCustomers={fetchCustomers}  onClose={onClose}/>
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

export default UpdateUserDrawer;