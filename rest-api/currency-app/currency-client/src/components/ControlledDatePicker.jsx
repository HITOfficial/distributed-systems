import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DemoContainer} from "@mui/x-date-pickers/internals/demo";

const  ControlledDatePicker = ({selectedDate, onChange}) => {

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DemoContainer components={['DatePicker', 'DatePicker']}>
                <DatePicker
                    label="Controlled picker"
                    value={selectedDate}
                    onChange={onChange}
                />
            </DemoContainer>
        </LocalizationProvider>
    );
}

export default ControlledDatePicker